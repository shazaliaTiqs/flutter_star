/** FlutterStarPrntPlugin */
class FlutterStarPrntPlugin : FlutterPlugin, MethodCallHandler {

    protected var starIoExtManager: StarIoExtManager? = null

    private lateinit var channel: MethodChannel

    companion object {
        lateinit var applicationContext: Context
    }

    override fun onAttachedToEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        applicationContext = binding.applicationContext
        channel = MethodChannel(binding.binaryMessenger, "flutter_star_prnt")
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull rawResult: Result) {
        val result: MethodResultWrapper = MethodResultWrapper(rawResult)
        Thread(MethodRunner(call, result)).start()
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    inner class MethodRunner(call: MethodCall, result: Result) : Runnable {
        private val call: MethodCall = call
        private val result: Result = result

        override fun run() {
            when (call.method) {
                "portDiscovery" -> portDiscovery(call, result)
                "checkStatus" -> checkStatus(call, result)
                "print" -> print(call, result)
                else -> result.notImplemented()
            }
        }
    }
}
