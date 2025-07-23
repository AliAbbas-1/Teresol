import streamlit as st
import requests

# Initialize theme in session state if not already set
if 'theme' not in st.session_state:
    st.session_state.theme = 'light' # Default theme

# Function to toggle the theme
def toggle_theme():
    if st.session_state.theme == 'light':
        st.session_state.theme = 'dark'
        st._config.set_option('theme.base', 'dark')
    else:
        st.session_state.theme = 'light'
        st._config.set_option('theme.base', 'light')
    st.rerun() # Rerun the app to apply the theme change

# Add a toggle button for the theme
# You can place this wherever you want your toggle to appear
st.sidebar.button(
    "Toggle Theme: " + ("☀️ Light" if st.session_state.theme == 'light' else "🌙 Dark"),
    on_click=toggle_theme
)

st.title("Hello")
res = requests.get("http://localhost:8000/")
res.raise_for_status()

st.json(res.json())