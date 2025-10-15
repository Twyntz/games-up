from typing import List, Dict, Optional, Any
from pathlib import Path
import pickle
import numpy as np
import pandas as pd
from sklearn.neighbors import NearestNeighbors
from models import UserData

MODEL_DIR = Path("/mnt/data/artifacts")
MODEL_DIR.mkdir(parents=True, exist_ok=True)
MODEL_PATH = MODEL_DIR / "knn_reco.pkl"

def _fallback_list() -> List[Dict[str, Any]]:
    return [
        {"game_id": 101, "game_name": "Pandemic"},
        {"game_id": 102, "game_name": "Catan"},
        {"game_id": 103, "game_name": "Ticket to Ride"}
    ]

class SimpleKNNRecommender:
    def __init__(self):
        self.model: Optional[NearestNeighbors] = None
        self.user_ids: Optional[np.ndarray] = None
        self.item_ids: Optional[np.ndarray] = None
        self.user_item: Optional[np.ndarray] = None

    # --- training des données ---
    def fit(self, df: pd.DataFrame, n_neighbors: int = 10, metric: str = "cosine", algorithm: str = "auto") -> None:
        if df is None or df.empty:
            raise ValueError("Training DataFrame is empty. Provide at least one interaction.")

        pivot = (
            df.pivot_table(index="user_id", columns="game_id", values="rating", aggfunc="mean")
            .fillna(0.0)
            .sort_index(axis=0)
            .sort_index(axis=1)
        )

        if pivot.shape[0] == 0 or pivot.shape[1] == 0:
            raise ValueError("Training data has no users or no items after pivot.")

        self.user_ids = pivot.index.to_numpy()
        self.item_ids = pivot.columns.to_numpy()
        self.user_item = pivot.to_numpy(dtype=float)

        n_neighbors = max(1, min(n_neighbors, len(self.user_ids)))
        self.model = NearestNeighbors(n_neighbors=n_neighbors, metric=metric, algorithm="auto")
        self.model.fit(self.user_item)

    # --- sauvegarde des données dans un volume ---
    def save(self) -> None:
        if self.model is None:
            return
        with open(MODEL_PATH, "wb") as f:
            pickle.dump(
                {
                    "user_ids": self.user_ids,
                    "item_ids": self.item_ids,
                    "user_item": self.user_item,
                    "model": self.model,
                },
                f,
            )

    def load(self) -> bool:
        if not MODEL_PATH.exists():
            return False
        with open(MODEL_PATH, "rb") as f:
            data = pickle.load(f)
        self.user_ids = data["user_ids"]
        self.item_ids = data["item_ids"]
        self.user_item = data["user_item"]
        self.model = data["model"]
        return True

    # --- recommendations ---
    def recommend(self, purchases: List[Dict[str, Any]], k: int = 10, user_id: Optional[int] = None) -> List[Dict[str, Any]]:
        # si on n'a pas de modèle entrainé, on renvoie la liste préremplie
        if self.model is None or self.user_item is None or self.item_ids is None:
            return _fallback_list()

        item_pos = {int(i): idx for idx, i in enumerate(self.item_ids)}
        seen = set()
        vec = None

        # si user_id connu mais pas d'achats, on utilise le vecteur utilisateur
        if (not purchases) and (user_id is not None) and (self.user_ids is not None):
            where = np.where(self.user_ids == user_id)[0]
            if where.size > 0:
                vec = self.user_item[where[0]]

        # sinon on construit un vecteur temporaire à partir des achats fournis
        if vec is None:
            tmp = np.zeros(len(self.item_ids), dtype=float)
            for p in purchases or []:
                gid = int(p.get("game_id"))
                rating = float(p.get("rating", 0.0))
                if gid in item_pos:
                    tmp[item_pos[gid]] = rating
                seen.add(gid)
            vec = tmp

        # si le vecteur est vide, on donne la popularité (moyenne par item), sinon KNN pondéré par similarité
        if float(vec.sum()) == 0.0:
            with np.errstate(invalid="ignore"):
                col_means = np.true_divide(self.user_item.sum(axis=0), (self.user_item != 0).sum(axis=0))
                scores = np.nan_to_num(col_means, nan=0.0)
        else:
            distances, indices = self.model.kneighbors(vec.reshape(1, -1), return_distance=True)
            neigh = self.user_item[indices[0]]
            sims = np.clip(1.0 - distances[0], 0.0, None)
            if sims.sum() > 0:
                weights = sims / sims.sum()
                scores = (neigh * weights[:, None]).sum(axis=0)
            else:
                scores = neigh.mean(axis=0)

        # exclut les items déjà vus
        for gid in seen:
            if gid in item_pos:
                scores[item_pos[gid]] = -np.inf

        # top-k
        top_idx = np.argsort(scores)[-k:][::-1]
        out = []
        for j in top_idx:
            if np.isneginf(scores[j]):
                continue
            out.append({"game_id": int(self.item_ids[j]), "score": float(scores[j])})
        # si tout est filtré, on retombe sur la liste préremplie
        return out or _fallback_list()

recommender = SimpleKNNRecommender()

def generate_recommendations(user_data: UserData):
    if recommender.model is None:
        recommender.load()

    purchases = [{"game_id": p.game_id, "rating": p.rating} for p in (getattr(user_data, "purchases", None) or [])]
    k = getattr(user_data, "k", 10)
    uid = getattr(user_data, "user_id", None)

    return recommender.recommend(purchases, k=k, user_id=uid)