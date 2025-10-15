from fastapi import FastAPI, HTTPException
from typing import List
from models import UserData, TrainPayload
from recommendation import recommender, generate_recommendations
from data_loader import interactions_to_dataframe

app = FastAPI(title="Recommandations KNN", version="0.1.0" )

# Endpoint de base pour tester que l'API est en ligne
@app.get("/")
async def root():
    return {"message": "API de recommandation en ligne"}

# Endpoint pour entraîner le modèle API. Exemple des données à envoyer :
'''
{
    "interactions": [
        {
            "user_id": 2,
            "game_id": 1,
            "rating": 4
        },
        {
            "user_id": 1,
            "game_id": 2,
            "rating": 5
        },
        {
            "user_id": 1,
            "game_id": 3,
            "rating": 2
        },
        {
            "user_id": 2,
            "game_id": 4,
            "rating": 1
        },
        {
            "user_id": 2,
            "game_id": 5,
            "rating": 4
        }
    ]
}
'''
@app.post("/train")
async def train(payload: TrainPayload):
    try:
        df = interactions_to_dataframe([i.dict() for i in payload.interactions])
        recommender.fit(df, n_neighbors=payload.n_neighbors, metric=payload.metric, algorithm=payload.algorithm)
        recommender.save()
        return {"status": "trained", "users": df.user_id.nunique(), "items": df.game_id.nunique()}
    except Exception as e:
        raise HTTPException(status_code=400, detail=str(e))

# Endpoint pour récupérer des recommandations. Exemple des données à envoyer :
'''
{
    "user_id": 1,
    "purchases": [
        {
            "game_id": 1,
            "rating": 5
        },
        {
            "game_id": 2,
            "rating": 4
        }
    ]
}
'''
@app.post("/recommendations")
async def get_recommendations(data: UserData):
    try:
        recommendations = generate_recommendations(data)
        return {"recommendations": recommendations}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))