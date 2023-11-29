Watch for changes in the markdown file and recompile the slides on change.
```bash
while true; do inotifywait -e close_write slides.md; pandoc -t slidy -s slides.md -o index.html; done
```