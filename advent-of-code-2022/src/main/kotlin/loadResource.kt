import java.nio.charset.Charset

fun loadResource(path: String): String {
  return Thread.currentThread()
      .contextClassLoader
      .getResourceAsStream(path)
      ?.reader(Charset.forName("UTF-8"))
      ?.readText()
      ?: error("Cannot read $path")
}
