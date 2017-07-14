package com.funlisten.base.event;

import com.funlisten.base.bean.ZYIBaseBean;
import com.funlisten.business.download.model.bean.ZYDownloadEntity;
import com.funlisten.service.downNet.down.ZYIDownBase;

/**
 * Created by ZY on 17/7/12.
 */

public class ZYEventDowloadUpdate implements ZYIBaseBean {

    public ZYIDownBase downloadEntity;

    public ZYEventDowloadUpdate(ZYIDownBase downloadEntity) {
        this.downloadEntity = downloadEntity;
    }
}
