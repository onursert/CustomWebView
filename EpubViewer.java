public class EpubViewer extends AppCompatActivity {
	
	CustomWebView webView;
    String title;
    String path;
	
	List<String> pagesRef = new ArrayList<>();
    List<String> pages = new ArrayList<>();
    int pageNumber = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_epub_viewer);
		title = getIntent().getStringExtra("title");
        getSupportActionBar().setTitle(title);
		
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.epubView);
		relativeLayout.setBackgroundColor(Color.WHITE);
		
		webView = (CustomWebView) findViewById(R.id.custom_WebView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setBackgroundColor(Color.TRANSPARENT);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				InjectJavascript();
			}
		});
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
				int pageCount = Integer.parseInt(message);
				webView.setPageCount(pageCount);
				result.confirm();
				return true;
			}
		});
		webView.loadUrl("file://" + pages.get(pageNumber));
	}

	public void pageDecrease() {
		if (pageNumber > 0) {
			pageNumber--;
			webView.loadUrl("file://" + pages.get(pageNumber));
		}
	}
	public void pageIncrease() {
		if (pageNumber < pages.size() - 1) {
			pageNumber++;
			webView.loadUrl("file://" + pages.get(pageNumber));
		}
	}

	private void InjectJavascript() {
		String js = "function initialize(){\n" +
				"    var d = document.getElementsByTagName('body')[0];\n" +
				"    var ourH = window.innerHeight;\n" +
				"    var ourW = window.innerWidth;\n" +
				"    var fullH = d.offsetHeight;\n" +
				"    var pageCount = Math.floor(fullH/ourH)+1;\n" +
				"    var currentPage = 0;\n" +
				"    var newW = pageCount*ourW;\n" +
				"    d.style.height = ourH+'px';\n" +
				"    d.style.width = newW+'px';\n" +
				"    d.style.webkitColumnGap = '10px'; " +
				"    d.style.margin = '0';\n" +
				"    d.style.webkitColumnCount = pageCount;\n" +
				"    return pageCount;\n" +
				"}";
		webView.loadUrl("javascript:" + js);
		webView.loadUrl("javascript:alert(initialize())");
	}
}
