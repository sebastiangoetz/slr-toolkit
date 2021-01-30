import CoreData
import SwiftUI

/// Root view of the app.
struct MainView: View {
    @State var project: Project?

    var body: some View {
        if project != nil {
            NavigationView {
                ProjectView(project: $project)
            }
        } else {
            WelcomeView(project: $project)
        }
    }
}

struct MainView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            NavigationView {
                MainView(project: nil)
            }
        }
    }
}
