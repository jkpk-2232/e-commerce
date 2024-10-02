package com.utils.myhub;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.jeeutils.DateUtils;
import com.utils.UUIDGenerator;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.ApnsDeviceModel;
import com.webapp.models.ApnsMessageModel;
import com.webapp.models.CustomerModel;
import com.webapp.models.CustomisedCustomerModel;
import com.webapp.models.CustomisedOrderItem;
import com.webapp.models.CustomisedOrderModel;
import com.webapp.models.OfflineOrderItemModel;
import com.webapp.models.OfflineOrderModel;
import com.webapp.models.OrderItemModel;
import com.webapp.models.OrderModel;
import com.webapp.models.VendorProductModel;
import com.webapp.models.VendorStoreModel;
import com.webapp.models.WebSocketClient;

public class CustomisedOrderUtils {

	private static Logger logger = Logger.getLogger(CustomisedOrderUtils.class);
	
	public static List<String> offlineOrders(String vendorId, String vendorStoreId, List<CustomisedOrderModel> customisedOrderList, String loggedInUserId) {
		
		List<String> successOrderIdsList = new ArrayList<>();
		
		for (CustomisedOrderModel customisedOrderModel : customisedOrderList) {
			
			OfflineOrderModel offlineOrder = OfflineOrderModel.getOrderDetailsByOfflineStoreOrderId(customisedOrderModel.getOrder());
			
			
			List<String> productSkuList = new ArrayList<>();
			for (CustomisedOrderItem customisedOrderItem : customisedOrderModel.getItems()) {
				productSkuList.add(customisedOrderItem.getSku());
			}
			
			List<VendorProductModel> vpmList = VendorProductModel.getProductDetailsByProductSkuAndVendorStoreIdList(productSkuList, vendorStoreId);
			
			if (offlineOrder == null  ) {
				
				if (vpmList.size() > 0) {
				
					VendorStoreModel vsm = VendorStoreModel.getVendorStoreDetailsById(vendorStoreId);
					
					String multicityCityRegionId = MultiCityAction.getMulticityRegionId(vsm.getStoreAddressLat(), vsm.getStoreAddressLng());
					
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
					SimpleDateFormat d = new SimpleDateFormat(DateUtils.DISPLAY_DATE_TIME_FORMAT);
					Date formattedDateTime = null;
					try {
						formattedDateTime = sdf.parse(customisedOrderModel.getDate());
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
					
					String orderId = UUIDGenerator.generateUUID();
					String OfflineOrderId = UUIDGenerator.generateUUID();
					
					OrderModel orderModel = new OrderModel();
					OfflineOrderModel offlineOrderModel = new OfflineOrderModel();
					
					
					long orderNumberOfItems = 0;
					
					List<OrderItemModel> orderItemList = new ArrayList<>();
					List<OfflineOrderItemModel> offlineOrderItemList = new ArrayList<>();
					
					for (VendorProductModel vendorProductModel : vpmList) {
						
						long numberOfItems = 0;
	
						for (CustomisedOrderItem customisedOrderItem : customisedOrderModel.getItems()) {
	
							if (customisedOrderItem.getSku().equalsIgnoreCase(vendorProductModel.getProductSku())) {
								
								
	
								numberOfItems = (long) customisedOrderItem.getQuantity();
								
								orderNumberOfItems = orderNumberOfItems + numberOfItems;
								
								if (!ProjectConstants.QuantityTypeConstants.LOOSE.equals(customisedOrderItem.getPrd_qty_type())) {
									long remainingProductInventoryCount = vendorProductModel.getProductInventoryCount() - numberOfItems;
									vendorProductModel.setProductInventoryCount(remainingProductInventoryCount);
								}
								
								
								OrderItemModel orderItemModel = new OrderItemModel();
								
								OfflineOrderItemModel offlineOrderItemModel = new OfflineOrderItemModel();
								
								orderItemModel.setOrderItemId(UUIDGenerator.generateUUID());
								orderItemModel.setOrderId(orderId);
								orderItemModel.setVendorProductId(vendorProductModel.getVendorProductId());
								orderItemModel.setVendorId(vendorProductModel.getVendorId());
								orderItemModel.setProductSku(vendorProductModel.getProductSku());
								orderItemModel.setProductCategory(vendorProductModel.getProductCategory());
								orderItemModel.setProductName(vendorProductModel.getProductName());
								orderItemModel.setProductInformation(vendorProductModel.getProductInformation());
								orderItemModel.setProductActualPrice(customisedOrderItem.getMrp());
								orderItemModel.setProductDiscountedPrice(customisedOrderItem.getPrice());
								orderItemModel.setProductWeight(vendorProductModel.getProductWeight());
								orderItemModel.setProductWeightUnit(vendorProductModel.getProductWeightUnit());
								orderItemModel.setProductSpecification(vendorProductModel.getProductSpecification());
								orderItemModel.setProductImage(vendorProductModel.getProductImage());
								orderItemModel.setPaid(vendorProductModel.isPaid());
								orderItemModel.setNumberOfItemsOrdered((int)numberOfItems);
								orderItemModel.setCreatedBy(vendorId);
								orderItemModel.setUpdatedBy(vendorId);
								
								if (formattedDateTime!= null) {
									orderItemModel.setCreatedAt(formattedDateTime.getTime());
									orderItemModel.setUpdatedAt(formattedDateTime.getTime());
									offlineOrderItemModel.setCreatedAt(formattedDateTime.getTime());
									offlineOrderItemModel.setUpdatedAt(formattedDateTime.getTime());
								}
								
								orderItemList.add(orderItemModel);
								
								
								offlineOrderItemModel.setOfflineOrderItemId(UUIDGenerator.generateUUID());
								offlineOrderItemModel.setOfflineStoreOrderItemId(customisedOrderItem.getId());
								offlineOrderItemModel.setProductName(customisedOrderItem.getProduct_name());
								offlineOrderItemModel.setProductSku(customisedOrderItem.getSku());
								offlineOrderItemModel.setPrice(customisedOrderItem.getPrice());
								offlineOrderItemModel.setMrp(customisedOrderItem.getMrp());
								offlineOrderItemModel.setQuantity(customisedOrderItem.getQuantity());
								offlineOrderItemModel.setOfflineOrderId(OfflineOrderId);
								offlineOrderItemModel.setCreatedBy(vendorId);
								offlineOrderItemModel.setUpdatedBy(vendorId);
								offlineOrderItemModel.setPrdQtyType(customisedOrderItem.getPrd_qty_type());
								
								offlineOrderItemList.add(offlineOrderItemModel);
	
							}
						}
						
					}
					orderModel.setOrderItemList(orderItemList);
	
					orderModel.setOrderId(orderId);
					orderModel.setOrderUserId(vendorId);
					
					if (formattedDateTime != null) {
						orderModel.setOrderCreationTime(formattedDateTime.getTime());
						offlineOrderModel.setDate(formattedDateTime.getTime());
					}
					
					orderModel.setOrderDeliveryLat(vsm.getStoreAddressLat());
					orderModel.setOrderDeliveryLng(vsm.getStoreAddressLng());
					orderModel.setVendorStoreId(vsm.getVendorStoreId());
					orderModel.setCreatedBy(vendorId);
					orderModel.setUpdatedBy(vendorId);
					orderModel.setOrderDeliveryAddress(vsm.getStoreAddress());
					orderModel.setOrderDeliveryStatus(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_OFFLINE);
					//orderModel.setOrderDeliveryAddressGeolocation(vsm.getStoreAddressGeolocation());
					//orderModel.setOrderPromoCodeDiscount(Double.parseDouble(BusinessAction.df.format(orderPromoCodeDiscount)));
					orderModel.setOrderTotal(customisedOrderModel.getTotal());
					//orderModel.setOrderDeliveryCharges(Double.parseDouble(BusinessAction.df.format(orderDeliveryCharges)));
					//orderModel.setOrderCharges(Double.parseDouble(BusinessAction.df.format(orderCharges)));
					//orderModel.setOrderDeliveryDistance(Double.parseDouble(BusinessAction.df.format(orderDeliveryDistance)));
					orderModel.setPaymentStatus(ProjectConstants.OrderDeliveryConstants.ORDER_PAYMENT_SUCCESS);
					orderModel.setOrderNumberOfItems((int)orderNumberOfItems);
					orderModel.setOrderReceivedAgainstVendorId(vendorId);
					orderModel.setMulticityCityRegionId(multicityCityRegionId);
					orderModel.setPaymentMode(ProjectConstants.CASH_ID);
					orderModel.setRecordStatus(ProjectConstants.ACTIVATE_STATUS);
					orderModel.setMulticityCountryId(ProjectConstants.DEFAULT_COUNTRY_ID);
					//orderModel.setCarTypeId(ProjectConstants.CAR_TYPES.BIKE_ID);
					orderModel.setServiceTypeId(ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID);
					orderModel.setOrderDeliveryAddressGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + vsm.getStoreAddressLat() + "  " + vsm.getStoreAddressLng() + ")')");
					if (formattedDateTime!= null) {
						orderModel.setCreatedAt(formattedDateTime.getTime());
						orderModel.setUpdatedAt(formattedDateTime.getTime());
						offlineOrderModel.setCreatedAt(formattedDateTime.getTime());
						offlineOrderModel.setUpdatedAt(formattedDateTime.getTime());
					}
					
					offlineOrderModel.setOfflineOrderItemList(offlineOrderItemList);
					
					offlineOrderModel.setOfflineOrderId(OfflineOrderId);
					offlineOrderModel.setOfflineStoreOrderId(customisedOrderModel.getOrder());
					offlineOrderModel.setRefNumber(customisedOrderModel.getRef_number());
					offlineOrderModel.setDiscount(customisedOrderModel.getDiscount());
					offlineOrderModel.setStatus(customisedOrderModel.getStatus());
					offlineOrderModel.setSubTotal(customisedOrderModel.getSubtotal());
					offlineOrderModel.setTax(customisedOrderModel.getTax());
					offlineOrderModel.setOrderType(customisedOrderModel.getOrder_type());
					offlineOrderModel.setPaymentType(customisedOrderModel.getPayment_type());
					offlineOrderModel.setPaymentInfo(customisedOrderModel.getPayment_info());
					offlineOrderModel.setTotal(customisedOrderModel.getTotal());
					offlineOrderModel.setPaid(customisedOrderModel.getPaid());
					offlineOrderModel.setChange(customisedOrderModel.getChange());
					offlineOrderModel.setCounterNo(customisedOrderModel.getTill());
					offlineOrderModel.setUserId(customisedOrderModel.getUser_id());
					offlineOrderModel.setCloudOrderId(customisedOrderModel.getCloud_order_id());
					offlineOrderModel.setCreatedBy(vendorId);
					offlineOrderModel.setUpdatedBy(vendorId);
					offlineOrderModel.setRoundedValue(customisedOrderModel.getRounded_value());
					
					CustomisedCustomerModel customisedCustomerModel = customisedOrderModel.getCustomer();
	
					CustomerModel customerModel = new CustomerModel();
					
					customerModel.setCustomerId(UUIDGenerator.generateUUID());
					customerModel.setPhoneNum(customisedCustomerModel.getId());
					customerModel.setCustomerName(customisedCustomerModel.getName());
					customerModel.setOfflineOrderId(OfflineOrderId);
					customerModel.setCreatedBy(vendorId);
					customerModel.setUpdatedBy(vendorId);
					if (formattedDateTime!= null) {
						customerModel.setCreatedAt(formattedDateTime.getTime());
						customerModel.setUpdatedAt(formattedDateTime.getTime());
					}
					
					
					orderModel = CustomisedOrderUtils.placeOfflineStoreOrder(orderModel, offlineOrderModel, customerModel);
					
					VendorProductModel.updateProductInventoryCount(loggedInUserId, vpmList);
					successOrderIdsList.add(customisedOrderModel.getOrder());
				}
			} else {
				successOrderIdsList.add(customisedOrderModel.getOrder());
			}
		}
		return successOrderIdsList;
	}
	
	
	private static OrderModel placeOfflineStoreOrder(OrderModel orderModel, OfflineOrderModel offlineOrderModel, CustomerModel customerModel) {
		
		orderModel = orderModel.insertOfflineStoreOrder(offlineOrderModel, customerModel);
		return orderModel;
		
	}


	public static void sendNewOrderNotificationToVendorViaSocket(OrderModel orderModel) {
		
		String vendorId = orderModel.getOrderReceivedAgainstVendorId();
		ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(vendorId);

		if (apnsDevice != null) {

			Map<String, Object> outPutMap = new HashMap<String, Object>();
			outPutMap.put("orderId", orderModel.getOrderId());
			outPutMap.put("orderModel", orderModel);

			JSONObject jsonMessage = new JSONObject(outPutMap);

			logger.info("\n\n\n\n\n\tNOR To Vendor\t" + jsonMessage);

			String messge = "TOUR`1`" + apnsDevice.getDeviceToken() + "`NOR," + jsonMessage.toString();
			WebSocketClient.sendDriverNotification(messge, vendorId, apnsDevice.getApiSessionKey());

		} else {
			logger.error("\nFailed to send NOR to vendor as apnsDevice is null");
		}

		String message = String.format(BusinessAction.messageForKeyAdmin("notificationVendorNewOrder"), orderModel.getOrderShortId());

		ApnsMessageModel apnsMessage = new ApnsMessageModel();
		apnsMessage.setMessage(message);
		apnsMessage.setMessageType("socket");
		apnsMessage.setToUserId(vendorId);
		apnsMessage.insertPushMessage();
		
	}


}
