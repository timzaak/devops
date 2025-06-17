#
to_camel() {
    name="$1"
    ext="${name##*.}"
    base="${name%.*}"

    camel=$(echo "$base" | sed -E 's/[^a-zA-Z0-9]+/ /g' | awk '{for(i=1;i<=NF;i++){ $i=toupper(substr($i,1,1)) substr($i,2)}; print}' | tr -d ' ')

    if [ "$base" != "$name" ]; then
        echo "$camel.$ext"
    else
        echo "$camel"
    fi
}

for file in *; do
    [ -f "$file" ] || continue
    newname=$(to_camel "$file")
    if [ "$file" != "$newname" ]; then
        mv "$file" "$newname"
        echo "Renamed $file -> $newname"
    fi
done