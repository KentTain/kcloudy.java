package kc.service;

import org.slf4j.Logger;

import java.util.function.Supplier;

import kc.service.base.ServiceResult;
import kc.service.enums.ServiceResultType;

public class ServiceWrapper {
	
	public static <T> ServiceResult<T> Invoke(String serviceName, String methodName,
			Supplier<T> func, Logger logger)
        {
            T result;
            String message = "";
            String detailsMsg = "调用服务(%s)的方法(%s)操作%s。";
            try
            {
                result = func.get();
                //message = String.Format(detailsMsg, serviceName, methodName, "成功");
                //logger.LogInfo(message);
                return new ServiceResult<T>(ServiceResultType.Success, "操作成功！", result);
            }
            catch (NullPointerException argnullex)
            {
                detailsMsg = String.format(detailsMsg, serviceName, methodName, "失败，错误消息为：" + argnullex.getMessage());
                logger.error(detailsMsg, argnullex);
                message = argnullex.getMessage();
                return new ServiceResult<T>(ServiceResultType.QueryNull, message);
            }
            catch (IllegalArgumentException argnullex)
            {
                detailsMsg = String.format(detailsMsg, serviceName, methodName, "失败，错误消息为：" + argnullex.getMessage());
                logger.error(detailsMsg, argnullex);
                message = argnullex.getMessage();
                return new ServiceResult<T>(ServiceResultType.ParamError, message);
            }
            catch (kc.framework.exceptions.ComponentException cex)
            {
                detailsMsg = String.format(detailsMsg, serviceName, methodName, "失败，错误消息为：" + cex.getMessage());
                logger.error(detailsMsg, cex);
                message = cex.getMessage();
                return new ServiceResult<T>(ServiceResultType.Error, message);
            }
            catch (kc.framework.exceptions.DataAccessException daex)
            {
                detailsMsg = String.format(detailsMsg, serviceName, methodName, "失败，错误消息为：" + daex.getMessage());
                logger.error(detailsMsg, daex);
                message = daex.getMessage();
                return new ServiceResult<T>(ServiceResultType.Error, message);
            }
            catch (kc.framework.exceptions.BusinessException bex)
            {
                detailsMsg = String.format(detailsMsg, serviceName, methodName, "失败，错误消息为：" + bex.getMessage());
                logger.error(detailsMsg, bex);
                return new ServiceResult<T>(ServiceResultType.Error, detailsMsg);
            }
            catch (kc.framework.exceptions.BusinessPromptException bpx)
            {
                detailsMsg = String.format(detailsMsg, serviceName, methodName, "失败，错误消息为：" + bpx.getMessage());
                logger.error(detailsMsg, bpx);
                return new ServiceResult<T>(ServiceResultType.Error, bpx.getMessage());
            }
            catch (kc.framework.exceptions.BusinessApiException bpx)
            {
                detailsMsg = String.format(detailsMsg, serviceName, methodName, "失败，错误消息为：" + bpx.getMessage());
                logger.error(detailsMsg, bpx);
                return new ServiceResult<T>(ServiceResultType.Error, bpx.getMessage());
            }
            catch (Exception ex)
            {
                detailsMsg = String.format(detailsMsg, serviceName, methodName, "失败，错误消息为：" + ex.getMessage());
                logger.error(detailsMsg, ex);
                message = ex.getMessage();
                return new ServiceResult<T>(ServiceResultType.Error, message);
            }
            finally
            {
//                watch.Stop();
//                logger.LogDebug(String.Format("%s：执行服务（%s）时间总共为: %s",
//                    serviceName, methodName, watch.ElapsedMilliseconds));
            }
        }
}
