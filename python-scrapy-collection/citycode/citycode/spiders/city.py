# -*- coding:utf-8 -*-

import scrapy
import re
from citycode.items import CitycodeItem

class CitySpider(scrapy.Spider):
    name = "city"

    def start_requests(self):
        urls = [
            'http://www.stats.gov.cn/tjsj/tjbz/xzqhdm/201703/t20170310_1471429.html'
        ]
        for url in urls:
            yield scrapy.Request(url=url, callback=self.parse)

    def parse(self, response):
        item = CitycodeItem()
        selector = scrapy.Selector(response)
        spans = selector.xpath('//div/p[@class="MsoNormal"]')
        for span in spans:
            t = span.xpath('b/span/text()').extract();
            if t:
                if len(t) == 2:
                    code = t[0]
                    city = t[1]
                    print 'code:' + code
                    print 'city:' + city
                    item['level'] = 0
                    item['code'] = code.strip()
                    item['city'] = city.strip()
                    yield item
            else:
                x = span.xpath('span/text()').extract()
                if x:
                    _blank = x[0]
                    code = x[1]
                    city = x[2]
                    level = 1
                    if len(_blank) == 1:
                        level = 1
                        print 'level:1'
                    elif len(_blank) == 2:
                        level = 2
                        print 'level:2'
                    else:
                        level = 3
                        print 'level:3'

                    print 'code:' + code
                    print 'city:' + city
                    item['level'] = level
                    item['code'] = code.strip()
                    item['city'] = city.strip()
                    yield item
