#!/bin/bash

set -e
set -m

pids=()

setup_and_run() {
    local dir="$1"
    echo "Setting up $dir..."

    cd "$dir"

    if [ ! -d "venv" ]; then
        echo "Creating virtual environment in $dir..."
        python3 -m venv venv
    fi

    source venv/bin/activate
    echo "Installing dependencies for $dir..."
    pip install --upgrade pip > /dev/null
    pip install -r requirements.txt

    echo "Running $dir/run.py..."
    python run.py &> "../${dir}.log" &
    local pid=$!

    deactivate
    cd ..

    echo "$dir started with PID $pid"
    pids+=($pid)
}

# Clean up on any exit signal
cleanup() {
    echo "Shutting down..."
    for pid in "${pids[@]}"; do
        kill "$pid" 2>/dev/null || true
    done
    wait "${pids[@]}" 2>/dev/null || true
    echo "All processes terminated."
}
trap cleanup SIGINT SIGTERM EXIT

# Start both apps
setup_and_run backend
setup_and_run frontend

# Wait for both
wait "${pids[@]}"
