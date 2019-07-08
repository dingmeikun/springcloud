## 归属地查询
***

请求参数：
{
	"ip": "192.168.101.1",
	"location": {
		"lat": "22.5260699992966",
		"lng": "113.94344999999997",
		"coordsys": "gaode"
	}
}

响应：对应经纬度位置信息
{
	"status": "1",
	"regeocode": {
		"addressComponent": {
			"city": "汉中市",
			"province": "陕西省",
			"adcode": "610724",
			"district": "西乡县",
			"towncode": "610724118000",
			"streetNumber": {
				"number": [],
				"direction": [],
				"distance": [],
				"street": []
			},
			"country": "中国",
			"township": "白勉峡镇",
			"businessAreas": [
				[]
			],
			"building": {
				"name": [],
				"type": []
			},
			"neighborhood": {
				"name": [],
				"type": []
			},
			"citycode": "0916"
		},
		"formatted_address": "陕西省汉中市西乡县白勉峡镇开花山"
	},
	"info": "OK",
	"infocode": "10000"
}