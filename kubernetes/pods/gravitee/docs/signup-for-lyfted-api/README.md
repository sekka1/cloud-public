Sign up for Lyfted.co's API
============================
This documentations shows you how to sign up for access to the Lyfted.co's API.

## 1) Sign up

Go to: https://developer.lyfted.co/#!/registration

and signup

![Sign Up Form](https://raw.githubusercontent.com/sekka1/cloud-public/master/kubernetes/pods/gravitee/docs/signup-for-lyfted-api/pics/signup-form.png)

## 2) Check for confirmation email:

An email will be sent to the email address you signed up with.  Check your
email for the confirmation and click on the link to complete your registration.

![Confirm](https://raw.githubusercontent.com/sekka1/cloud-public/master/kubernetes/pods/gravitee/docs/signup-for-lyfted-api/pics/confirm-email.png)

## 3) Confirm your account and set your password

This will bring you to a page to confirm your account information and set your password

![Sign Up Form](https://raw.githubusercontent.com/sekka1/cloud-public/master/kubernetes/pods/gravitee/docs/signup-for-lyfted-api/pics/confirm-account.png)

## 4) Sign up for the Lyfted API
Once logged in, click on the `API Gallery` on the top left.

![API Gallery](https://raw.githubusercontent.com/sekka1/cloud-public/master/kubernetes/pods/gravitee/docs/signup-for-lyfted-api/pics/api-gallery.png)

###Select a plan

![Select a Plan](https://raw.githubusercontent.com/sekka1/cloud-public/master/kubernetes/pods/gravitee/docs/signup-for-lyfted-api/pics/select-a-plan.png)

### Plan Setup

![Plan Setup](https://raw.githubusercontent.com/sekka1/cloud-public/master/kubernetes/pods/gravitee/docs/signup-for-lyfted-api/pics/plan-setup.png)

Once you have setup your plan, and created it.  It will give you a `cURL` string similar to:

```
curl -X GET "https://gateway.lyfted.co/lyfted" -H "X-Gravitee-Api-Key: your-unique-key"
```

This has your `API key` which you will use for accessing the Lfted.co API.
