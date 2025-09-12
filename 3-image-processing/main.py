import pytesseract
import cv2

image = cv2.imread("sample.png")

cv2.imshow("Test Image", image)
cv2.waitKey(0)

cv2.destroyAllWindows()

text = pytesseract.image_to_string(image)

print("Extracted Text:", text)
