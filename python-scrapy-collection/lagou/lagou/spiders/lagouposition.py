# -*- coding: utf-8 -*-
import scrapy
import json
from lagou.items import LagouItem

class LagoupositionSpider(scrapy.Spider):
    name = "lagouposition"
    # allowed_domains = ["https://www.lagou.com/jobs/list_?px=new"]
    # start_urls = ['https://www.lagou.com/jobs/list_?px=new&city=北京#filterBox']
    start_urls = ['https://www.lagou.com/jobs/positionAjax.json?px=new&city=北京&needAddtionalResult=false']

    totalPageCount = 0
    curpage = 1

    kds = [u'大数据',u'云计算',u'docker',u'中间件']
    kd = kds[3]

    reqUrl = 'https://www.lagou.com/jobs/positionAjax.json?px=new&city=北京&needAddtionalResult=false'

    def start_requests(self):
        return [scrapy.http.FormRequest(self.reqUrl,
                                        formdata = {'pn': str(self.curpage), 'kd': self.kd}, callback = self.parse)]

    def parse(self, response):
        # print response.body
        # fp = open('1.html', 'w')
        # fp.write(response.body)
        # fp.close()
        jdict = json.loads(response.body)
        jcontent = jdict['content']
        jposresult = jcontent["positionResult"]
        jresult = jposresult["result"]

        self.totalPageCount = jposresult['totalCount'] / 15 + 1;
        print self.totalPageCount

        item = LagouItem();

        for each in jresult:
            # print each['city']
            # print each['companyFullName']
            # print each['companySize']
            # print each['positionName']
            # print each['firstType']
            # print each['salary']
            # print ''
            item['city'] = each['city']
            item['companyFullName'] = each['companyFullName']
            item['companySize'] = each['companySize']
            item['positionName'] = each['positionName']
            item['firstType'] = each['firstType']
            item['salary'] = each['salary']
            yield item

        if self.curpage <= self.totalPageCount:
            self.curpage += 1
            yield scrapy.http.FormRequest(self.reqUrl,
                                            formdata = {'pn': str(self.curpage), 'kd':self.kd}, callback = self.parse)
