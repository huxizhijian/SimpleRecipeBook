package org.huxizhijian.sdk.util.adapter.listadapter.base;


import org.huxizhijian.sdk.util.adapter.listadapter.ViewHolder;


/**
 * Created by zhy on 16/6/22.
 */
public interface ItemViewDelegate<T>
{

    public abstract int getItemViewLayoutId();

    public abstract boolean isForViewType(T item, int position);

    public abstract void convert(ViewHolder holder, T t, int position);



}
