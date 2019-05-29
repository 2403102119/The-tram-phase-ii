package com.tangchaoke.electrombilechargingpile.bean;

import java.util.List;

/**
 * Title:数据
 * Author：李迪迦
 * Date:2019.5.17
 */

public class Defian {
    //账户明细
    public class AccountAll{
        public List<content> content;
        public boolean last;
        public String pageable;
        public int totalPages;
        public int totalElements;
        public boolean first;
        public Sort sort;
        public int numberOfElements;
        public int size;
        public int number;
        public boolean empty;


    }

    //账户明细 2
    public class content{
        public String id;
        public String roleId;
        public String roleAccount;
        public String roleType;
        public String type;
        public String money;
        public String paymentMethod;
        public String payOrderNumber;
        public String paymentStatus;
        public long paymentTime;


    }
    public class Sort{
        public boolean sorted;
        public boolean unsorted;
        public boolean empty;

    }

}
