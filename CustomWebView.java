public class CustomWebView extends WebView {
	
    float x1 = -1;
    int pageCount = 0;
    int current_x = 0;
    int currentPage = 0;

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float x2 = event.getX();
                float deltaX = x2 - x1;
                if (Math.abs(deltaX) > 100) {
                    if (x2 > x1) {
                        turnPageLeft();
                        return true;
                    }
                    else {
                        turnPageRight();
                        return true;
                    }
                }
                break;
        }
        return true;
    }

    private void turnPageLeft() {
        if (currentPage > 0) {
            int scrollX = (currentPage-=1) * (this.getMeasuredWidth()+2);
            loadAnimation(scrollX);
            current_x = scrollX;
            scrollTo(scrollX, 0);
        }
        else {
            EpubViewer.getInstance().pageDecrease();
        }
    }

    private void turnPageRight() {
        if (currentPage < pageCount - 1) {
            int scrollX = (currentPage+=1) * (this.getMeasuredWidth()+2);
            loadAnimation(scrollX);
            current_x = scrollX;
            scrollTo(scrollX, 0);
        }
        else {
            EpubViewer.getInstance().pageIncrease();
        }
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    private void loadAnimation(int scrollX) {
        ObjectAnimator anim = ObjectAnimator.ofInt(this, "scrollX", current_x, scrollX);
        anim.setDuration(500);
        anim.setInterpolator(new LinearInterpolator());
        anim.start();
    }
}
