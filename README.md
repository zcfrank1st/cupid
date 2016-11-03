Cupid
===
> Cupid is funny !

* Description

    This is a standalone backend of a push system which is using udp protocal to send push content and using http protocal to communicate with clients.
    
* Usage
    * Use udp client (java version, ios version whatever) to connect.
    * Use restful apis to push message, pull message and register heartbeat.

* Api

    POST /push
    
        {
            “uniqueId”: [string],
            “content”: [string],
            “schedule”: [string] // date millseconds
        }
        
    POST /heartbeat
    
        {
            "uniqueId":[string],
            "values": [string] // ip:port
        }
    
    GET /pull/{uniqueId}


    