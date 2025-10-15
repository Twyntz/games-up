from pydantic import BaseModel, Field
from typing import List

class UserPurchase(BaseModel):
    game_id: int
    rating: float = Field(ge=1, le=5)

class UserData(BaseModel):
    user_id: int
    purchases: List[UserPurchase]

class Interaction(BaseModel):
    user_id: int
    game_id: int
    rating: float = Field(ge=1, le=5)

class TrainPayload(BaseModel):
    interactions: List[Interaction] = Field(min_items=1)
    n_neighbors: int = 5
    metric: str = "cosine"
    algorithm: str = "auto"