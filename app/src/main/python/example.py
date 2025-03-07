# import cv2
# import numpy as np
#
# # Load an image (make sure to use a valid path in the Android environment)
# image = cv2.imread("/path/to/image.jpg")
#
# # Convert image to grayscale
# gray_image = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
#
# # Save or process the image further
# cv2.imwrite("/path/to/output.jpg", gray_image)


def factorial(a):
    fact=1
    for i in range(1,a+1):
        fact *=i
    return fact
number =55
text="Himanshu Chauhan"