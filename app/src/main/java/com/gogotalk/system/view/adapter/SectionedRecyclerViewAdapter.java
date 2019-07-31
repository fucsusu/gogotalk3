package com.gogotalk.system.view.adapter;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public abstract class SectionedRecyclerViewAdapter<H extends RecyclerView.ViewHolder,
        VH extends RecyclerView.ViewHolder,
        F extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected static final int TYPE_SECTION_HEADER_ONE = -1;
    protected static final int TYPE_SECTION_HEADER_TWO = -4;
    protected static final int TYPE_SECTION_FOOTER = -2;
    protected static final int TYPE_ITEM = -3;

    //用四个数组，对应保存每一个单项（包括header和footer）的四个属性
    //1.哪个分组；2.在该组的位置；3.是否是头部；4.是否是底部；
    private int[] sectionForPosition = null;
    private int[] positionWithinSection = null;
    private boolean[] isHeader = null;
    private boolean[] isFooter = null;
    private int count = 0;

    public SectionedRecyclerViewAdapter() {
        super();
        registerAdapterDataObserver(new SectionDataObserver());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        setupIndices();
    }

    /**
     * Returns the sum of number of items for each section plus headers and footers if they
     * are provided.
     */
    @Override
    public int getItemCount() {
        return count;
    }

    private void setupIndices() {
        count = countItems();
        allocateAuxiliaryArrays(count);
        precomputeIndices();
    }

    private int countItems() {
        int count = 0;
        int sections = getSectionCount();
        //计算总个数
        for (int i = 0; i < sections; i++) {
            count += 1 + getItemCountForSection(i) + (hasFooterInSection(i) ? 1 : 0);
        }
        return count;
    }

    //为每一个单项，设置四大属性
    private void precomputeIndices() {
        int sections = getSectionCount();
        int index = 0;

        for (int i = 0; i < sections; i++) {
            if (hasHeaderInSection(i)) {
                setPrecomputedItem(index, true, false, i, 0);
                index++;
            }

            for (int j = 0; j < getItemCountForSection(i); j++) {
                setPrecomputedItem(index, false, false, i, j);
                index++;
            }

            if (hasFooterInSection(i)) {
                setPrecomputedItem(index, false, true, i, 0);
                index++;
            }
        }
    }

    private void allocateAuxiliaryArrays(int count) {
        sectionForPosition = new int[count];
        positionWithinSection = new int[count];
        isHeader = new boolean[count];
        isFooter = new boolean[count];
    }

    private void setPrecomputedItem(int index, boolean isHeader, boolean isFooter, int section, int position) {
        this.isHeader[index] = isHeader;
        this.isFooter[index] = isFooter;
        sectionForPosition[index] = section;
        positionWithinSection[index] = position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
//        isSectionHeaderViewType01(viewType)||
        if (isSectionHeaderViewType02(viewType)) {
            viewHolder = onCreateSectionHeaderViewHolder(parent, viewType);
        } else if (isSectionFooterViewType(viewType)) {
            viewHolder = onCreateSectionFooterViewHolder(parent, viewType);
        } else {
            viewHolder = onCreateItemViewHolder(parent, viewType);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int section = sectionForPosition[position];
        int index = positionWithinSection[position];

        if (isSectionHeaderPosition(position)) {
            onBindSectionHeaderViewHolder((H) holder, section);
        } else if (isSectionFooterPosition(position)) {
            onBindSectionFooterViewHolder((F) holder, section);
        } else {
            onBindItemViewHolder((VH) holder, section, index);
        }

    }

    @Override
    public int getItemViewType(int position) {

        if (sectionForPosition == null) {
            setupIndices();
        }

        int section = sectionForPosition[position];
        int index = positionWithinSection[position];

        if (isSectionHeaderPosition(position)) {
            return getSectionHeaderViewType(section);
        } else if (isSectionFooterPosition(position)) {
            return getSectionFooterViewType(section);
        } else {
            return getSectionItemViewType(section, index);
        }

    }

    protected int getSectionHeaderViewType(int section) {
//        if(section==0)return TYPE_SECTION_HEADER_ONE;
        return TYPE_SECTION_HEADER_TWO;
    }

    protected int getSectionFooterViewType(int section) {
        return TYPE_SECTION_FOOTER;
    }

    protected int getSectionItemViewType(int section, int position) {
        return TYPE_ITEM;
    }

    /**
     * Returns true if the argument position corresponds to a header
     */
    public boolean isSectionHeaderPosition(int position) {
        if (isHeader == null) {
            setupIndices();
        }
        return isHeader[position];
    }

    /**
     * Returns true if the argument position corresponds to a footer
     */
    public boolean isSectionFooterPosition(int position) {
        if (isFooter == null) {
            setupIndices();
        }
        return isFooter[position];
    }

    protected boolean isSectionHeaderViewType01(int viewType) {
        return viewType == TYPE_SECTION_HEADER_ONE;
    }
    protected boolean isSectionHeaderViewType02(int viewType) {
        return viewType == TYPE_SECTION_HEADER_TWO;
    }
    protected boolean isSectionFooterViewType(int viewType) {
        return viewType == TYPE_SECTION_FOOTER;
    }

    /**
     * Returns the number of sections in the RecyclerView
     */
    protected abstract int getSectionCount();

    /**
     * Returns the number of items for a given section
     */
    protected abstract int getItemCountForSection(int section);

    /**
     * Returns true if a given section should have a footer
     */
    protected abstract boolean hasFooterInSection(int section);

    protected abstract boolean hasHeaderInSection(int section);
    /**
     * Creates a ViewHolder of class H for a Header
     */
    protected abstract H onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType);

    /**
     * Creates a ViewHolder of class F for a Footer
     */
    protected abstract F onCreateSectionFooterViewHolder(ViewGroup parent, int viewType);

    /**
     * Creates a ViewHolder of class VH for an Item
     */
    protected abstract VH onCreateItemViewHolder(ViewGroup parent, int viewType);

    /**
     * Binds data to the header view of a given section
     */
    protected abstract void onBindSectionHeaderViewHolder(H holder, int section);

    /**
     * Binds data to the footer view of a given section
     */
    protected abstract void onBindSectionFooterViewHolder(F holder, int section);

    /**
     * Binds data to the item view for a given position within a section
     */
    protected abstract void onBindItemViewHolder(VH holder, int section, int position);

    class SectionDataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            setupIndices();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            setupIndices();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            setupIndices();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            setupIndices();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            setupIndices();
        }
    }
}