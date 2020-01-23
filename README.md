# AndroidOpenCvSample

Sample application to use OpenCV in Android.

* Create empty Android application
* Download and extract desired release of **Android** OpenCV from https://opencv.org/releases (e.g., 4.2.0)

* Import OpenCV as a module
	* File->New->Import Module...
	* Choose 'opencv/sdk' directory and optionally rename module (e.g., 'openCvLibrary420')
	* Add 'openCvLibrary420/build' to .gitignore
* Copy '\<openCvDir\>/sdk/native/libs' directory to '\<androidAppDir>/app/src' and rename to 'jniLibs'

* Add dependency
	* File->Project Structure...
	* Select 'Dependencies' on left-hand side
	* Select 'app' module and click '+' button
	* Click checkbox for 'openCvLibrary420'

* Load the OpenCV library from MainActivity:
<pre><code>
	static {
		System.loadLibrary("opencv_java4");
	}
</pre></code>