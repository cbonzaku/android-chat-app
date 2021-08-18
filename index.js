var crypto = require('crypto');
var uuid = require('uuid');
var express =require('express');
var http = require('http');
var mysql = require('mysql');
var bodyParser = require('body-parser');
const { Server } = require("socket.io");
const appr= express();
const server = http.createServer(appr);
const io = new Server(server);



//serving html
appr.get('/', (req, res) => {
    console.log("weeee");
    res.json({ greeting: 'hello!!!!' })
  });

 
  Array.prototype.indexOfId = function(ID) {
    for (var i = 0; i < this.length; i++)
        if (this[i].userID == ID)
            return i;
    return -1;
}


//sql connction
var con=mysql.createConnection({
    host:'localhost',
    user:'root',
    password:'',
    database:'chat_app'
});



//SOCKET 
var users = [];
io.on('connection', (socket) => {
    console.log('a user connected');
    socket.on("createConversation",(convName)=>{
        socket.join(convName);
        con.query("insert into conversation (name) values(?)",[convName]);
    });

    socket.on ("joinConv",(convid, convname,userID)=>{
        
        con.query("insert into conve_user  values(?,?)",[userID,convid]);
        socket.join(convname);
    });
    
    console.log('a user connected');
    console.log('userid:',socket.id);
    
    socket.on("user",(user1,uid)=> {

        console.log("user:"+user1);
            users.push({
              userID: uid,
              username:user1
            });
        console.log("users:%j",users);
        io.emit("users", users);
         
      });

      socket.on('loadConv',(userID)=>{
        con.query("select * from conversation   inner join conve_user"+
          "on conversation.convID = conve_user.convID where users.userID=?",[userID],(err,result,fields)=>{
            if(err) {throw err;}
            socket.emit("loadedConv",{result});
            for(let i=0;i<result.length;i++){
                socket.join(result[i].name);
            }
        });  
      });


      socket.on("sendMessage", (message,targetID,targetu) => {
        var indexOfSender=users.indexOfId(socket.id);
        var u=users[indexOfSender].username;
        var mtime = new Date();
        var hour = mtime.getHours();
        var minutes = mtime.getMinutes();
        var string_time = hour+":"+minutes;
        //con.query("insert into messages (contenue,date,coversation,senderID) values(?,?,?,?)",[message,string_time,conv,u])
          io.to(targetID).emit("sendMessage", {
          message:message,
          from: u,
          to:targetu,
          id:socket.id,
          time:string_time
        });
      });


      socket.on("seen",(from,conversationID)=>{

      });


      socket.on("disconnect",()=>{
        console.log("destroyed");
          var ind = users.indexOfId(socket.id);
          console.log("value :",ind);
          if (ind>-1) 
          {
              users.splice(ind,1);
              io.emit("users", users);
             
          }
          
      })
        
  });




  //PASSWORD
var genRandomString= function(length){
      return crypto.randomBytes(Math.ceil(length/2))
      .toString('hex')
      .slice(0,length)
  };
var sha512 =function(password,salt){
    var hash = crypto.createHmac('sha512',salt);
    hash.update(password);
    var value =hash.digest('hex');
    return{
        salt:salt,
        passwordHash:value
    };
};

function saltHashPassword(userPassW){
    var salt = genRandomString(16);
    var passwordData=sha512(userPassW,salt);
    return passwordData;

}

appr.use(bodyParser.json());
appr.use(bodyParser.urlencoded({extended:true}));


server.listen(3000,()=>{
    console.log("rserver running at port 8000!!");
})
appr.get("/g",(req,res,next)=>{
    var en = saltHashPassword("123456");
    console.log('password:123456');
    console.log('encrypt: '+en.passwordHash);
})
appr.get('/parkings', (req,res) => {
    res.send("weeeee");
})


// login rout
appr.post("/login",(req,res)=>{
    let k = req.body;
    var username = k.username;
    var user_passworxd=k.password;


    if(username && user_passworxd){
    con.query('select username from users where username=? and password=? ',
        [username,user_passworxd],(err,result,fields)=>{
            if(result.length>0){
            res.json({token:"login!!"})}
            else{
                res.send("wrong username or password")
            }});
    
         }else
         res.send('Please enter Username and Password!');});




appr.post("/register",(req,res)=>{
    let k = req.body;
    var username = k.username;
    var user_passworxd=k.password;
    
            if(username && user_passworxd){
                con.query('select username from users where username=? ',
                    [username,user_passworxd],(err,result,fields)=>{
                        if(result.length>0){
                        res.send("username already exist!!!")}
                        else{
                            con.query('INSERT INTO users (username, password) VALUES (?, ?) ',
                    [username,user_passworxd],(err,result,fields)=>{
                        if(!err){
                            res.send("welcom!!! "+username)
                        }
                    })

                        }});
                
                     }
        
})

appr.post("/newconv",(req,res)=>{
    var body=req.body;
    var user1=body.user
    var usertarget =body.target;
    con.query('select * from users where username=?',[usertarget],(err,result,fields)=>{
        if(result.length>0){
            con.query('insert into conversation (user1,user2) values (?,?)',[user1,usertarget])
        }else{res.json({message:"user does not exist"})}
    })
})