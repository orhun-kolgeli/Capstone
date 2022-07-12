package com.orhunkolgeli.capstone;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    // The minimum amount of items to have below the current scroll position before loading more
    private final int VISIBLE_THRESHOLD = 5;
    // The current offset index of data loaded
    private int currentPage = 0;
    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;
    // True if still waiting for the last set of data to load
    private boolean loading = true;
    // Starting page index
    private final int START_PAGE_INDEX = 0;

    RecyclerView.LayoutManager layoutManager;

    public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0 || lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView view, int dx, int dy) {
        int totalItemCount = layoutManager.getItemCount();
        int lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();

        // If the total item count is zero and the previous is not, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.START_PAGE_INDEX;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }
        // If still loading, check to see if the dataset count has
        // changed, if so,conclude it has finished loading and update the current page
        // number and total item count
        if (this.loading && (totalItemCount > this.previousTotalItemCount)) {
            this.loading = false;
            this.previousTotalItemCount = totalItemCount;
        }

        // If not currently loading, check to see if we have breached
        // the VISIBLE_THRESHOLD and need to reload more data
        // If we do need to reload some more data, execute onLoadMore to fetch the data.
        if (!this.loading && (lastVisibleItemPosition + VISIBLE_THRESHOLD) > totalItemCount) {
            currentPage++;
            onLoadMore(currentPage, totalItemCount, view);
            this.loading = true;
        }
    }

    public void resetState() {
        // Reset scrollListener's state to perform new searches
        this.currentPage = this.START_PAGE_INDEX;
        this.previousTotalItemCount = 0;
        this.loading = true;
    }

    // Defines the process for loading more data based on page
    public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view);

}
