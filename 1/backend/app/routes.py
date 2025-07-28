from datetime import datetime
from functools import wraps
from fastapi import APIRouter
from fastapi.responses import JSONResponse, RedirectResponse
from app.logic import get_meta_info, get_calculation_data, get_file_data
from app.logic import train_and_predict

router = APIRouter()

def safe_symbol_access(func):
    @wraps(func)
    def wrapper(*args, **kwargs):
        symbol = kwargs.get("symbol", "").upper()
        kwargs["symbol"] = symbol
        try:
            return func(*args, **kwargs)
        except KeyError:
            return JSONResponse(status_code=404, content={"error": f"Symbol '{symbol}' not found"})
        except FileNotFoundError:
            return JSONResponse(status_code=404, content={"error": f"Data file for symbol '{symbol}' not found"})
    return wrapper

@router.get("/favicon.ico")
def get_favicon():
    return RedirectResponse(url="/static/favicon.ico")

@router.get("/stocks")
def get_stocks():
    df = get_meta_info().copy()
    df["Symbol"] = df.index

    cols = df.columns.tolist()
    cols = ["Symbol"] + [col for col in cols if col != "Symbol"]    
    df = df[cols]

    return df.to_dict(orient="records")

@router.get("/stocks/unique")   
def get_stocks_unique():
    return {
        "unique_records": len(get_meta_info().to_dict(orient="records"))
    }

@router.get("/stocks/{symbol}")
@safe_symbol_access
def get_stocks_symbol(symbol: str):
    symbol = symbol.upper()

    df_meta = get_meta_info()
    return df_meta.loc[symbol].to_dict()
    
@router.get("/stocks/{symbol}/data")
@safe_symbol_access
def get_stocks_symbol_data(symbol: str):
    symbol = symbol.upper()

    df = get_file_data(symbol).copy()

    df["Date"] = df.index

    cols = df.columns.tolist()
    cols = ["Date"] + [col for col in cols if col != "Date"]    
    df = df[cols]

    return df.to_dict(orient="records")
    
@router.get("/stocks/{symbol}/data/{date}")
@safe_symbol_access
def get_stocks_symbol_data_date(symbol: str, date: datetime):
    symbol = symbol.upper()
    
    df_file = get_file_data(symbol)

    try:
        return df_file.loc[date].to_dict()
    except KeyError:
        return JSONResponse(status_code=404, content={"error": f"Date '{date.date()}' not found for symbol '{symbol}'"})

@router.get("/stocks/{symbol}/analytics")
@safe_symbol_access
def get_stocks_symbol_analytics(symbol: str):
    symbol = symbol.upper()

    return get_calculation_data(get_file_data(symbol))

@router.get("/stocks/{symbol}/predict")
def predict_stock_price(symbol: str):
    df = get_file_data(symbol).copy()
    df['Date'] = df.index
    return train_and_predict(df)

@router.get("/distribution/etf")
def get_distribution_etf():
    df = get_meta_info()
    return df["ETF"].value_counts().to_dict()

@router.get("/distribution/exchanges")
def get_distribution_exchanges():
    df = get_meta_info()

    return df["Listing Exchange"].value_counts().to_dict()

@router.get("/distribution/categories")
def get_distribution_categories():
    df = get_meta_info()
    return df["Market Category"].value_counts().to_dict()