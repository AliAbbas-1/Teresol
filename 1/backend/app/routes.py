from fastapi import APIRouter, UploadFile, File
from app.logic import get_all_data

router = APIRouter()

@router.get("/")
def test():
    return get_all_data().to_dict(orient="records")

@router.get("/{symbol}")
def get_record_from_symbol(symbol: str):
    symbol = symbol.upper()
    df = get_all_data()

    for _, row in df.iterrows():
        if str(row["Symbol"]).upper() == symbol:
            return row.to_dict()
    return {"error": f"Symbol '{symbol}' not found"}

@router.post("/upload")
async def upload_file(file: UploadFile = File(...)):
    upload = await file.read()
    return {"status": "success"}

# TODO
# Create endpoints for every function in app.logic