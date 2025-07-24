import io
import pandas as pd
from fastapi import APIRouter, UploadFile, File
from app.logic import get_all_metadata, set_df_file

router = APIRouter()

@router.get("/unique_stocks")
def get_num_unique_stocks():
    df = get_all_metadata()
    return {"num_stocks": f"{df['Symbol'].nunique()}"}

@router.get("/stocks/{symbol}")
def get_record_from_symbol(symbol: str):
    symbol = symbol.upper()
    df = get_all_metadata()

    try:
        return df.loc[symbol.upper()].to_dict()
    except KeyError:
        return {"error": f"Symbol '{symbol}' not found"}

# TODO
# Create endpoints for every function in app.logic