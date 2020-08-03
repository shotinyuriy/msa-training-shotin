local function start()
    box.schema.space.create("somedata")
    box.space.somedata:create_index("primary")
end

return {
  start = start;
}