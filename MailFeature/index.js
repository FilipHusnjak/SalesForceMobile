const sgMail = require('@sendgrid/mail');
sgMail.setApiKey('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');
const msg = {
  to: 'filip.husnjak98@gmail.com',
  from: 'salesforcemobile@salesforcemobile.com',
  subject: 'Reset Password',
  html: '<p>We received a request to change your password on Sales Force Mobile.</p><p>Click the link below to set a new password:</p><p><a href="https://google.com">New password</a></p><p>If you don\'t want to change your password, please ignore this email.</p><p>Thank you, <br/> Sales Force Mobile team</p>'
};
sgMail.send(msg);
console.log("Sent");