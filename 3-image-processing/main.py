import pytesseract
import cv2

# Load the image
image = cv2.imread("sample.png")

# Show the image in a window
cv2.imshow("Test Image", image)

# Wait for a key press (0 = wait forever until you press a key)
cv2.waitKey(0)

# Close the image window
cv2.destroyAllWindows()

# Extract text using OCR
text = pytesseract.image_to_string(image)
+
print("Extracted Text:", text)