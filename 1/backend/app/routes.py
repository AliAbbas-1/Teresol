from fastapi import APIRouter
from app.logic import get_meta_info, get_calculation_data, get_file_info

router = APIRouter()

@router.get("/stocks")
def get_all_symbols():
    return get_meta_info().to_dict(orient="records")

@router.get("/stocks/unique")
def get_unique_symbols():
    return {
        "unique_records": len(get_meta_info().to_dict(orient="records"))
    }

@router.get("/stocks/{symbol}")
def get_record_from_symbol(symbol: str):
    symbol = symbol.upper()

    try:
        df = get_meta_info()
        return {
            "meta": df.loc[symbol].to_dict(),
            "calc": get_calculation_data(get_file_info(symbol))
        }
    except KeyError:
        print("symbol")
        return {"error": f"Symbol '{symbol}' not found"}
    
    except FileNotFoundError:
        print("file")
        return {"error": f"Data file for symbol '{symbol}' not found"}

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

# TODO
# Create endpoints for every function in app.logic