# -*- coding: utf-8 -*-
import scrapy
import json
from hospitalcrawler.items import HospitalcrawlerItem

class HospitalSpider(scrapy.Spider):
    name = "hospital"
    allowed_domains = ["https://www.hqms.org.cn/usp/roster/rosterInfo.jsp"]
    start_urls = [
    # 三甲医院
    'https://www.hqms.org.cn/usp/roster/rosterInfo.jsp?provinceId=7182&htype=&hgrade=1&hclass=1&hname=&_=1502504216949',

    # 三级非甲
    'https://www.hqms.org.cn/usp/roster/rosterInfo.jsp?provinceId=7182&htype=&hgrade=1&hclass=2&hname=&_=1502505282061',
    'https://www.hqms.org.cn/usp/roster/rosterInfo.jsp?provinceId=7182&htype=&hgrade=1&hclass=3&hname=&_=1502505256891',
    'https://www.hqms.org.cn/usp/roster/rosterInfo.jsp?provinceId=7182&htype=&hgrade=1&hclass=3&hname=&_=1502505298724',
    'https://www.hqms.org.cn/usp/roster/rosterInfo.jsp?provinceId=7182&htype=&hgrade=1&hclass=4&hname=&_=1502505316824',
    'https://www.hqms.org.cn/usp/roster/rosterInfo.jsp?provinceId=7182&htype=&hgrade=1&hclass=5&hname=&_=1502505330004',
    'https://www.hqms.org.cn/usp/roster/rosterInfo.jsp?provinceId=7182&htype=&hgrade=1&hclass=6&hname=&_=1502505346896',

    # 二级综合
    'https://www.hqms.org.cn/usp/roster/rosterInfo.jsp?provinceId=7182&htype=A1&hgrade=2&hclass=&hname=&_=1502505543268'

    ]

    def parse(self, response):
        item = HospitalcrawlerItem()
        print "Start"

        jdict = json.loads(response.body)
        for each in jdict:
            print each["hName"]
            print each["hGrade"]
            item["name"] = each["hName"]
            item["grade"] = each["hGrade"]
            item["type"] = each["hType"].strip()

            yield item

        print "End"
