package pkg;
public class Test {
protected MimeMessage criarMimeMessage(Mensagem mensagem) throws MessagingException {
String corpo = processarTemplate(mensagem);
MimeMessage mimeMessage = mailSender.createMimeMessage();
MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
helper.setFrom(emailProperties.getRemetente());
helper.setTo(mensagem.getDestinatarios().toArray(new String[0]));
helper.setSubject(mensagem.getAssunto());
helper.setText(corpo, true);
return mimeMessage;
}
}