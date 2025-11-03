package com.angel.minio;




import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Test;




@SpringBootTest
class MFileServerApplicationTests {






    @Test
    void contextLoads() {

      for(int i=0;i<6;i++){
          System.out.println("测试："+Integer.toString(i));
          if(i==1){
              break;
          }
      }

    }


}
