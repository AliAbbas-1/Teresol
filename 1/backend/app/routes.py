import io
import pandas as pd
from fastapi import APIRouter, UploadFile, File
from app.logic import get_meta_file, set_df_file, get_distribution_data, get_file

router = APIRouter()

@router.get("/unique_stocks")
def get_num_unique_stocks():
    df = get_meta_file()
    return {"num_stocks": f"{df['Symbol'].nunique()}"}

@router.get("/stocks/{symbol}")
def get_record_from_symbol(symbol: str):
    symbol = symbol.upper()
    df = get_meta_file()

    try:
        return df.loc[symbol.upper()].to_dict()
    except KeyError:
        return {"error": f"Symbol '{symbol}' not found"}
    
@router.get("/distribution/{symbol}")
def get_distribution_from_symbol(symbol: str):
    symbol = symbol.upper()
    return get_distribution_data(get_file())

# TODO
# Create endpoints for every function in app.logic