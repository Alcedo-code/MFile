function createLayerFromData(data) {
  // 假设data是一个包含图层相关信息的对象
  // 根据data创建并返回一个图层对象
  const layer = new Layer(data.id, data.name, data.data)
  return layer
}
