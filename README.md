Cupid
===
![cupid](logo/cupid.png)
> Cupid is funny !

* Description

    This is a standalone backend of a push system which is using udp protocal to communicate with clients and using http protocal to manage push contents.
    
* Usage
    * Use udp client (java version, ios version whatever) to connect and register heartbeat every ? seconds.
    * Use restful apis to push message, pull messages.

* Api

    POST /push
    
        {
            “uniqueId”: [string],
            “content”: [string],
            “schedule”: [string] // date millseconds
        }
        
    
    GET /pull/{uniqueId}
    
* Client

    java: [psyche](https://github.com/zcfrank1st/psyche)
    
   
* License

    MIT

    


    