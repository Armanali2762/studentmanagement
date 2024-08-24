package com.example.demo.Entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Comment;

import lombok.Data;

@Entity
@Data
@Table(name = "query_list_table_field")
public class QueryListFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk")
    private Long pk;

    @Column(name = "query_list_pk")
    private Long queryListPk;

    @Column(name = "table_field", length = 5000)
    private String tableField;

    @Column(name = "`order`", columnDefinition = "tinyint(2)")
    private Integer order;

    @Column(name = "display_name", length = 100)
    private String displayName;

    @Column(name = "width", length = 10)
    private String width;

    @Column(name = "edit_delete_use", columnDefinition = "tinyint")
    private Integer editDeleteUse;

    @Column(name = "right_align", columnDefinition = "tinyint default 0")
    private Integer rightAlign;

    @Column(name = "is_orderable", columnDefinition = "tinyint default 1")
    private Integer isOrderable;

    @Column(name = "is_searchable", columnDefinition = "tinyint default 1")
    private Integer isSearchable;

    @Comment("0 for unformatted column, 1 for formatted column")
    @Column(name = "is_formated_column", columnDefinition = "tinyint default 0")
    private Integer isFormatedColumn;

    @Column(name = "non_formated_search_column", length = 1000)
    private String nonFormatedSearchColumn;

    @Comment("0 for text box,1 for drop down")
    @Column(name = "searchable_type", columnDefinition = "tinyint default 0")
    private Integer searchableType;

    @Column(name = "start_top_header")
    private Integer startTopHeader;

    @Column(name = "rowspan")
    private Integer rowspan;

    @Column(name = "top_header_pk")
    private Long topHeaderPk;

    @Column(name = "is_alias_filter", columnDefinition = "tinyint default 1")
    private Integer isAliasFilter;

    @Comment("0=Not visible default, 1=visible default")
    @Column(name = "is_visible", columnDefinition = "tinyint default 1")
    private Integer isVisible;

    @Column(name = "is_date_column", length = 45)
    private String isDateColumn;
}
