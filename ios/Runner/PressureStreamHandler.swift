//
//  PressureStreamHandler.swift
//  Runner
//
//  Created by Cynotecck on 27/07/23.
//

import Foundation
import CoreMotion

class PressureStreamHandler : NSObject, FlutterStreamHandler
{
    let altiMeter = CMAltimeter()
    private let queue = OperationQueue()
    
    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        if CMAltimeter.isRelativeAltitudeAvailable()
        {
            altiMeter.startAbsoluteAltitudeUpdates(to: queue ){
                (data, error) in
                if data != nil{
                    let pressurePascal = data?.pressure
                    events(pressurePascal!.doubleValue * 10.0 )
                    
                }
            }
        }
    }
    
    func onCancel(withArguments arguments: Any?) -> FlutterError? {
        <#code#>
    }
    
   
}
