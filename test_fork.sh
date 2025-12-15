#!/bin/bash
echo "Testing Fork Example..."
echo "11" | java -cp out Main | grep -A 20 "Fork example"
echo ""
