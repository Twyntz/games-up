import pandas as pd
from typing import List, Dict, Any

def load_training_data_from_csv(file_path: str) -> pd.DataFrame:
    # Ex: Charge un fichier CSV contenant les données d'utilisateur, jeux, etc.
    return pd.read_csv(file_path)

def interactions_to_dataframe(interactions: List[Dict[str, Any]]) -> pd.DataFrame:
    # Convertir les interactions en DataFrame avec user_id, game_id, rating.
    return pd.DataFrame(interactions)