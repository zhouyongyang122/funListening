package com.funlisten.base.event;

import com.funlisten.base.bean.ZYIBaseBean;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;

/**
 * Created by ZY on 17/7/12.
 */

public class ZYEventDowloadUpdate implements ZYIBaseBean {

    public ZYDownloadEntity downloadEntity;

    public ZYEventDowloadUpdate(ZYDownloadEntity downloadEntity) {
        this.downloadEntity = downloadEntity;
    }
}
