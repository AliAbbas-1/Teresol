from fastapi import APIRouter

router = APIRouter()

@router.get("/")
def test():
    return {"hello": "world"}

@router.get("/test")
def other_test():
    return {"world": "hello"}