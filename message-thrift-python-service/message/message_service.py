# coding=utf-8
from message.api import MessageService
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer

import smtplib
from email.mime.text import MIMEText
from email.header import Header

sender = "975129758@qq.com"
authCode = "mndxjiuinelzbdgf"


class MessageServiceHandler:

    def sendMobileMessage(self, mobile, message):
        print ("sendMobileMessage")
        return True

    def sendEmailMessage(self, email, message):
        print ("sendEmailMessage, email:" + email + ", message:" + message)
        messageObj = MIMEText(message, "plain", "utf-8")
        messageObj["From"] = sender
        messageObj["To"] = email
        messageObj["Subject"] = Header('ZZG注册邮件', 'utf-8')
        try:
            smtpObj = smtplib.SMTP('smtp.qq.com')
            smtpObj.login(sender, authCode)
            smtpObj.sendmail(sender, [email], messageObj.as_string())
            print ("send mail success")
            return True
        except smtplib.SMTPException:
            print ("send mail failed!")
            return False


if __name__ == '__main__':
    handler = MessageServiceHandler()
    processor = MessageService.Processor(handler)
    transport = TSocket.TServerSocket(None, "9090")
    tfactory = TTransport.TFramedTransportFactory()
    pfactory = TBinaryProtocol.TBinaryProtocolFactory()

    server = TServer.TSimpleServer(processor, transport, tfactory, pfactory)
    print ("python thrift server start")
    server.serve()
    print ("python thrift server exit")
