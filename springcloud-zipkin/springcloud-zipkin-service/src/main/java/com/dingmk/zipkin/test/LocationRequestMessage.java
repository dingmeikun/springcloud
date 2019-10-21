/******************************************************************************* 
 * Copyright (C) Microfountain Technology, Inc. All Rights Reserved. 
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited.   
 * Proprietary and confidential
 ******************************************************************************/
package com.dingmk.zipkin.test;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationRequestMessage {

	/** imsi */
	private String imsi;
    
	/** iccid */
	private String iccid;
    
	/** 短信中心号 */
	private String cnum;
	
	public boolean validMessage() {
		
		boolean validImsi = (null != imsi && !(imsi = imsi.trim()).isEmpty());
		boolean validIccid = (null != iccid && !(iccid = iccid.trim()).isEmpty());
		boolean validCnum = (null != cnum && !(cnum = cnum.trim()).isEmpty());
		
		// 只要有一个不为空 则判定合法
		return (validImsi || validIccid || validCnum);
	}
}
