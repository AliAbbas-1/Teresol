import os

if __name__ == "__main__":
    os.execvp("streamlit", [
        "streamlit",
        "run",
        "app/main.py",
        "--server.port", "8001",
        "--server.headless", "true"
        ]
    )