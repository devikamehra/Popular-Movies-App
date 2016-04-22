package awe.devikamehra.popularmoviesapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Devika on 19-02-2016.
 */
public class Response {

    @SerializedName("id")
    public Long id;

    @SerializedName("page_no")
    public Integer pageNo;

    @SerializedName("results")
    public ArrayList<SingleResponse> responses;

    @SerializedName("total_pages")
    public Integer totalPages;

    @SerializedName("total_results")
    public Integer totalResults;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public ArrayList<SingleResponse> getResponses() {
        return responses;
    }

    public void setResponses(ArrayList<SingleResponse> responses) {
        this.responses = responses;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }
}
