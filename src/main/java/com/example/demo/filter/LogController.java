package com.example.demo.filter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class LogController {

	@RequestMapping("show")
	@ResponseBody
	public String poll(){
		LogQueen queen = LogQueen.getInstance();
		boolean flag = true;
		int i = 0;
		while(flag){
			LogMessage msg = queen.poll();
			System.out.println(msg);
			i++;
			if(i == 10){
				flag=false;
			}
			return msg.toString();
		}
		return null;
	}
	
}
