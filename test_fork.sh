#!/bin/bash
echo "Testing Fork Example..."
echo "11" | java -cp out Main | grep -A 20 "Fork example"
echo ""
echo "Checking log file..."
tail -20 log11.txt 2>/dev/null || echo "Log file not created yet"