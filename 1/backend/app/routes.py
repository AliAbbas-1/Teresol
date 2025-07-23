from fastapi import APIRouter

router = APIRouter()

@router.get("/")
def test():
    return {"hello": "world"}

# TODO
# Create endpoints for every function in app.logic