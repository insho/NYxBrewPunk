package com.inshodesign.nytimesreader;


        import android.animation.Animator;
        import android.animation.ObjectAnimator;
        import android.app.Activity;
        import android.graphics.Point;
        import android.graphics.Typeface;
        import android.graphics.drawable.ColorDrawable;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.v4.content.ContextCompat;
        import android.text.Spannable;
        import android.text.SpannableString;
        import android.text.SpannableStringBuilder;
        import android.text.method.LinkMovementMethod;
        import android.text.style.ForegroundColorSpan;
        import android.text.style.StyleSpan;
        import android.text.style.URLSpan;
        import android.util.Log;
        import android.util.TypedValue;
        import android.view.Display;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.PopupWindow;
        import android.widget.ProgressBar;
        import android.widget.TextView;

        import com.inshodesign.nytimesreader.Models.BrewDogBeer;
        import com.inshodesign.nytimesreader.Models.NYTimesArticle;
        import com.squareup.picasso.Callback;
        import com.squareup.picasso.Picasso;
        import java.util.ArrayList;


/**
 * Popup with information for a BrewDog beer. Appears when user clicks on a beer in
 * {@link com.inshodesign.nytimesreader.Fragments.ArticleListFragment}. Article title
 * is matched to a beer, and info is pulled from BrewDog api  in
 * {@link com.inshodesign.nytimesreader.Fragments.ArticleListFragment#getBeerData(String, NYTimesArticle)}
 *
 * User can drag down to dismiss the popup.
 */
public class BeerInfoPopup implements View.OnTouchListener {

    private final String TAG = "WordDetailPopupWindow";

    private Activity activity;
    private View anchorView;
    private final NYTimesArticle mArticle;
    private final BrewDogBeer mBeerData;
    private String mNameMatch = "Name Match";

    public BeerInfoPopup(Activity activity, View anchorView, @NonNull NYTimesArticle article, @Nullable BrewDogBeer beerData, @NonNull String namematch) {
        this.activity = activity;
        this.anchorView = anchorView;
        this.mArticle = article;
        this.mBeerData = beerData;
        this.mNameMatch = namematch;
    }

    private View baseLayout;
    private PopupWindow popupWindow;
    private int previousFingerPosition = 0;
    private int baseLayoutPosition = 0;
    private int defaultViewHeight;
    private boolean isClosing = false;
    private boolean isScrollingUp = false;
    private boolean isScrollingDown = false;


    public View CreateView() {
        View popupView = LayoutInflater.from(activity).inflate(R.layout.popup_layout2, null);
        baseLayout = popupView.findViewById(R.id.popuptab_layout);
        baseLayout.setOnTouchListener(this);

        final TextView articleTitle = (TextView) popupView.findViewById(R.id.articleTitle);
        SpannableString text2 = new SpannableString(mArticle.getTitle());
        text2.setSpan(new URLSpan(mArticle.getUrl()), 0, mArticle.getTitle().length(), 0);
        text2.setSpan(new ForegroundColorSpan(ContextCompat.getColor(activity, android.R.color.white)), 0, mArticle.getTitle().length(), 0);
        articleTitle.setMovementMethod(LinkMovementMethod.getInstance());
        articleTitle.setText(text2, TextView.BufferType.SPANNABLE);
        articleTitle.setTypeface(null, Typeface.BOLD);

        //Make the title text height equal to the actionbar height
        TypedValue tv = new TypedValue();
        if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            articleTitle.setMinHeight(TypedValue.complexToDimensionPixelSize(tv.data,activity.getResources().getDisplayMetrics()));
        }

        TextView statusbarspacer = (TextView) popupView.findViewById(R.id.status_bar_spacer);
            int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusbarspacer.setHeight(activity.getResources().getDimensionPixelSize(resourceId));
            }

        //Fill beer info with text spans that are half-unformatted, half-bold
        ((TextView) popupView.findViewById(R.id.recommendedBeer)).setText(boldSpan("Recommended beer: ",mBeerData.getName()));
        ((TextView) popupView.findViewById(R.id.recommendationcriteria)).setText(boldSpan("Recommended on: ",mNameMatch));
        ((TextView) popupView.findViewById(R.id.abv)).setText(boldSpan("abv: ",mBeerData.getABV()));
        ((TextView) popupView.findViewById(R.id.ibu)).setText(boldSpan("ibu: ",mBeerData.getIBU()));


//        TextView recommendedBeer = (TextView) popupView.findViewById(R.id.recommendedBeer);
//        recommendedBeer.setText(boldSpan("Recommended beer: ",mBeerData.getName()));
//
//
//        final SpannableStringBuilder sb = new SpannableStringBuilder("Recommended beer: " + mBeerData.getName());
//        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD); // Span to make text bold
//         sb.setSpan(bss, 18, (18 + mBeerData.getName().length()), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        recommendedBeer.setText(sb);
//

//        TextView recommendationCriteria = (TextView) popupView.findViewById(R.id.recommendationcriteria);
//        final SpannableStringBuilder sbCriteria = new SpannableStringBuilder("Recommended on: " + mNameMatch);
//        sbCriteria.setSpan(bss, 16, (16 + mNameMatch.length()), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        recommendationCriteria.setText(sbCriteria);
//
//        TextView abv =  (TextView) popupView.findViewById(R.id.abv);
//        final SpannableStringBuilder sbABV = new SpannableStringBuilder("abv: " + mBeerData.getABV());
//        sbCriteria.setSpan(bss, 5, (5 + mBeerData.getABV().length()), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        abv.setText(sbABV);
//
//        TextView ibu =  (TextView) popupView.findViewById(R.id.ibu);
//        final SpannableStringBuilder sbIBU = new SpannableStringBuilder("ibu: " + mBeerData.getIBU());
//        sbCriteria.setSpan(bss, 5, (5 + mBeerData.getIBU().length()), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        ibu.setText(sbIBU);


        TextView beerInfo = (TextView) popupView.findViewById(R.id.beerInfo);
        beerInfo.setText(mBeerData.getDescription());
        beerInfo.setTypeface(null, Typeface.ITALIC);

        ImageView beerImage = (ImageView) popupView.findViewById(R.id.beerImage) ;
        ProgressBar bar = (ProgressBar) popupView.findViewById(R.id.progressBar);
        bar.setVisibility(View.VISIBLE);
        final TextView loadingText = (TextView) popupView.findViewById(R.id.loadingtext);
        loadingText.setVisibility(View.VISIBLE);
        beerImage.setVisibility(View.VISIBLE);
        final ProgressBar progressView = bar;

        final Callback loadedCallback = new Callback() {
            @Override public void onSuccess()
            {
                progressView.setVisibility(View.GONE);
                loadingText.setVisibility(TextView.GONE);
            }

            @Override public void onError() {
                Log.e(TAG,"ERROR LOADING sweet beer image");
                progressView.setVisibility(View.GONE);
                loadingText.setVisibility(TextView.GONE);

            } };

        beerImage.setTag(loadedCallback);

        Picasso.with(activity)
                .load(mBeerData.getImage_url())
                .resize(250, 300)
                .centerInside()
                .into(beerImage, loadedCallback);

        baseLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        popupWindow = new PopupWindow(baseLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.getContentView().setFocusableInTouchMode(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);

        int location[] = new int[2];

        anchorView.getLocationOnScreen(location);
        popupWindow.showAtLocation(baseLayout, Gravity.NO_GRAVITY, 0, 180);

        return popupView;
    }

    /**
     * Takes two strings and combines them so that one section is unformatted and another bold
     * @param plainText text to keep unformatted
     * @param boldText text to make bold
     * @return a span with partly bold (ex: Recommended beer: THE BEER)
     */
    private SpannableStringBuilder boldSpan(String plainText, String boldText) {
        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        final SpannableStringBuilder sb = new SpannableStringBuilder(plainText + boldText);
        sb.setSpan(bss, plainText.length(), (plainText.length() + boldText.length()), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        return sb;
    }

    public boolean isShowing() {
        return popupWindow.isShowing();
    }

    /**
     * Tracks the user holding and dragging the Popup window. If they drag far enough down
     * the screen the popup will dismiss
     */
    public boolean onTouch(View view, MotionEvent event) {

        // Get finger position on screen
        final int Y = (int) event.getRawY();

        // Switch on motion event type
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                // save default base layout height
                defaultViewHeight = baseLayout.getHeight();
                previousFingerPosition = Y;
                baseLayoutPosition = (int) baseLayout.getY();
                break;

            case MotionEvent.ACTION_UP:
                // If user was doing a scroll up
                if (isScrollingUp) {
                    // Reset baselayout position
                    baseLayout.setY(0);
                    // We are not in scrolling up mode anymore
                    isScrollingUp = false;
                }

                // If user was doing a scroll down
                if (isScrollingDown) {
                    // Reset baselayout position
                    baseLayout.setY(0);
                    // Reset base layout size
                    baseLayout.getLayoutParams().height = defaultViewHeight;
                    baseLayout.requestLayout();
                    // We are not in scrolling down mode anymore
                    isScrollingDown = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isClosing) {
                    int currentYPosition = (int) baseLayout.getY();

                    // If we scroll up
                    if (previousFingerPosition > Y) {
                        // First time android rise an event for "up" move
                        if (!isScrollingUp) {
                            isScrollingUp = true;
                        }

                        // Has user scroll down before -> view is smaller than it's default size -> resize it instead of change it position
                        if (baseLayout.getHeight() < defaultViewHeight) {
                            baseLayout.getLayoutParams().height = baseLayout.getHeight() - (Y - previousFingerPosition);
                            baseLayout.requestLayout();
                        }
                        baseLayout.setY(baseLayout.getY() + (Y - previousFingerPosition));

                    }
                    // If we scroll down
                    else {
                        // First time android rise an event for "down" move
                        if (!isScrollingDown) {
                            isScrollingDown = true;
                        }

                        // Has user scroll enough to "auto close" popup ?
                        if (Math.abs(baseLayoutPosition - currentYPosition) > defaultViewHeight / 6) {
                            closeDownAndDismissDialog(currentYPosition);
                            return true;
                        }

                        // Change base layout size and position (must change position because view anchor is top left corner)
                        baseLayout.setY(baseLayout.getY() + (Y - previousFingerPosition));
                        baseLayout.getLayoutParams().height = baseLayout.getHeight() - (Y - previousFingerPosition);
                        baseLayout.requestLayout();
                    }

                    // Update position
                    previousFingerPosition = Y;
                }
                break;


        }

        return true; //gestureDetector.onTouchEvent(event);
    }

    /**
     * Sends dialog layout off the bottom of the screen in an animation and
     * dismisses the dialog
     * @param currentPosition position on the screen
     */
    private void closeDownAndDismissDialog(int currentPosition) {
        isClosing = true;

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenHeight = size.y;
        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(baseLayout, "y", currentPosition, screenHeight + baseLayout.getHeight());
        positionAnimator.setDuration(300);
        positionAnimator.addListener(new Animator.AnimatorListener() {
            //            . . .
            @Override
            public void onAnimationStart(Animator animation) {

            }


            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                popupWindow.dismiss();

                //reset the position variables
                previousFingerPosition = 0;
                baseLayoutPosition = 0;
                isClosing = false;
                isScrollingUp = false;
                isScrollingDown = false;

            }
        });
        positionAnimator.start();
    }



}

