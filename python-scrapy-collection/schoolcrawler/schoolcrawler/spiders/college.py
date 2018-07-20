# -*- coding: utf-8 -*-
import scrapy
import re
from schoolcrawler.collegeitem import CollegecrawlerItem

class CollegeSpider(scrapy.Spider):
    name = "college"

    start_urls = [
    'http://www.cunet.com.cn/daxue/HTML/238575.html'
    ]

    def parse(self, response):
        item = CollegecrawlerItem()
        print "Start"
        selector = scrapy.Selector(response)
        schools = selector.xpath('//div[@class="wrap_centerfont"]/table[1]/tbody[1]/tr/td[2]')
        for school in schools:
            name = school.xpath('text()').extract()[0];
            print name
            item['name'] = name

            yield item

        print "End"
