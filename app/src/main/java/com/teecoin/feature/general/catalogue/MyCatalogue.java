package com.teecoin.feature.general.catalogue;

import com.teecoin.R;
import com.teecoin.model.couponsystem.FilterCatalogueModel;
import com.teecoin.model.couponsystem.VendorCategoryModel;
import com.teecoin.utils.EnumMgr;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

public class MyCatalogue {

    private ArrayList<VendorCategoryModel> listCategorySort;

    private ArrayList<FilterCatalogueModel> listFilterCatalogue;

    public MyCatalogue() {
        listCategorySort = new ArrayList<>();
        listFilterCatalogue = new ArrayList<>();
    }

    public ArrayList<FilterCatalogueModel> getListFilterCatalogue() {
        return listFilterCatalogue;
    }

    public ArrayList<VendorCategoryModel> getListCategorySort() {
        return listCategorySort;
    }

    public void setListCategoryToSort(ArrayList<VendorCategoryModel> listCategorySort) {
        this.listCategorySort = listCategorySort;

    }

    public VendorCategoryModel getVendorCategorySelected() {
        VendorCategoryModel vendorCategoryModel = new VendorCategoryModel();
        if (listCategorySort != null && listCategorySort.size() > 0) {
            for (VendorCategoryModel categoryModel : listCategorySort) {
                if (categoryModel.isSelector())
                    vendorCategoryModel = categoryModel;
                return vendorCategoryModel;
            }
        }
        return vendorCategoryModel;
    }

    public void getListFilterCategory() {
        //if (getListFilterCatalogue().size() > 0)
          //  return;
        listFilterCatalogue = new ArrayList<>();
        listFilterCatalogue.add(new FilterCatalogueModel(EnumMgr.SortByTypeCoupon.GoodDeals.getValue(), TCUtils.getString(R.string.good_deals), "", false));
        listFilterCatalogue.add(new FilterCatalogueModel(EnumMgr.SortByTypeCoupon.Coupons.getValue(), TCUtils.getString(R.string.text_popular), "", true));
        listFilterCatalogue.add(new FilterCatalogueModel(EnumMgr.SortByTypeCoupon.NewArrivals.getValue(), TCUtils.getString(R.string.new_arrivals), "", false));
    }

    public void updateListFilter(FilterCatalogueModel filterCatalogueModel) {
        if (filterCatalogueModel == null || listFilterCatalogue == null)
            return;
        for (FilterCatalogueModel filter : listFilterCatalogue) {
            if (filterCatalogueModel.getId().equals(filter.getId())) {
                filter.setSelector(filterCatalogueModel.isSelector());
            } else {
                filter.setSelector(false);
            }
        }
    }
}
