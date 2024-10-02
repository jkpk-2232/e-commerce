package com.webapp.daos;

import com.webapp.models.AdminTipModel;

public interface AdminTipDao {

	int updateAdminTip(AdminTipModel adminTipModel);

	AdminTipModel getAdminTipByAdminId(String adminId);

	AdminTipModel getAdminTip();
}
